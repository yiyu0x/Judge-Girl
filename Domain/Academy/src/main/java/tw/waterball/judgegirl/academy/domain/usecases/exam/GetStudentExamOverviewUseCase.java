package tw.waterball.judgegirl.academy.domain.usecases.exam;

import lombok.AllArgsConstructor;
import tw.waterball.judgegirl.academy.domain.repositories.ExamRepository;
import tw.waterball.judgegirl.primitives.exam.Exam;
import tw.waterball.judgegirl.primitives.exam.IpAddress;
import tw.waterball.judgegirl.primitives.exam.Question;
import tw.waterball.judgegirl.primitives.exam.Record;
import tw.waterball.judgegirl.primitives.problem.Problem;
import tw.waterball.judgegirl.problemapi.clients.ProblemServiceDriver;
import tw.waterball.judgegirl.problemapi.views.ProblemView;

import javax.inject.Named;
import javax.validation.Valid;
import java.util.Optional;

import static tw.waterball.judgegirl.commons.exceptions.NotFoundException.notFound;

@Named
public class GetStudentExamOverviewUseCase extends AbstractExamUseCase {
    private final ProblemServiceDriver problemService;

    public GetStudentExamOverviewUseCase(ExamRepository examRepository, ProblemServiceDriver problemService) {
        super(examRepository);
        this.problemService = problemService;
    }

    public void execute(Request request, Presenter presenter) {
        int studentId = request.studentId;
        Exam exam = findExam(request.examId);
        onlyWhitelistIpAddressExamineeCanAccessTheOngoingExam(request, exam);
        presenter.showExam(exam);

        for (Question question : exam.getQuestions()) {
            findProblem(question)
                    .ifPresentOrElse(problem -> {
                        presenter.showQuestion(question, problem);
                        showRemainingQuotaOfQuestion(presenter, studentId, question);
                        findBestRecord(studentId, question)
                                .ifPresentOrElse(record -> {
                                    int yourScore = question.calculateScore(record);
                                    presenter.showBestRecordOfQuestion(record);
                                    presenter.showYourScoreOfQuestion(question, yourScore);
                                }, () -> presenter.showYourScoreOfQuestion(question, 0));
                    }, () -> presenter.showNotFoundQuestion(question));
        }
    }

    private void onlyWhitelistIpAddressExamineeCanAccessTheOngoingExam(Request request, Exam exam) {
        if (request.isStudent && exam.isOngoing() && !exam.hasIpAddress(new IpAddress(request.ipAddress))) {
            throw notFound(Exam.class).id(request.examId);
        }
    }

    private Optional<Problem> findProblem(Question question) {
        return problemService.getProblem(question.getId().getProblemId())
                .map(ProblemView::toEntity);
    }

    private Optional<Record> findBestRecord(int studentId, @Valid Question question) {
        return examRepository.findRecordOfQuestion(question.getId(), studentId);
    }

    private void showRemainingQuotaOfQuestion(Presenter presenter, int studentId, Question question) {
        int answerCount = examRepository.countAnswersInQuestion(question.getId(), studentId);
        presenter.showRemainingQuotaOfQuestion(question, question.getQuota() - answerCount);
    }

    public interface Presenter {
        void showExam(Exam exam);

        void showQuestion(Question question, Problem problem);

        void showBestRecordOfQuestion(Record bestRecord);

        void showYourScoreOfQuestion(Question question, int yourScore);

        void showRemainingQuotaOfQuestion(Question question, int remainingQuota);

        void showNotFoundQuestion(Question question);
    }

    @AllArgsConstructor
    public static class Request {
        public int examId;
        public boolean isStudent;
        public int studentId;
        public String ipAddress;
    }
}
