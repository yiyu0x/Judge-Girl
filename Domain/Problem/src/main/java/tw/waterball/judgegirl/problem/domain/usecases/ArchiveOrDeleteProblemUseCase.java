package tw.waterball.judgegirl.problem.domain.usecases;

import tw.waterball.judgegirl.primitives.problem.Problem;
import tw.waterball.judgegirl.problem.domain.repositories.ProblemRepository;

import javax.inject.Named;

@Named
public class ArchiveOrDeleteProblemUseCase extends BaseProblemUseCase {

    public ArchiveOrDeleteProblemUseCase(ProblemRepository problemRepository) {
        super(problemRepository);
    }

    public void execute(int problemId) {
        problemRepository.findProblemById(problemId)
                .ifPresent(this::archiveOrDeleteProblem);
    }

    private void archiveOrDeleteProblem(Problem problem) {
        if (problem.isArchived()) {
            problemRepository.deleteProblemById(problem.getId());
        } else {
            problemRepository.archiveProblemById(problem.getId());
        }
    }

}