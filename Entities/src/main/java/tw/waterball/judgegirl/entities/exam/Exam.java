package tw.waterball.judgegirl.entities.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.waterball.judgegirl.commons.utils.JSR380Utils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static tw.waterball.judgegirl.commons.utils.StreamUtils.findFirst;
import static tw.waterball.judgegirl.entities.date.DateProvider.now;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exam {

    private Integer id = null;

    @NotBlank
    private String name;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private String description;

    @Valid
    private List<Question> questions = new ArrayList<>();

    public Exam(String name, Date startTime, Date endTime, String description) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public void validate() {
        JSR380Utils.validate(this);
        if (startTime.after(endTime)) {
            throw new IllegalStateException();
        }
    }

    public boolean isUpcoming() {
        return getStartTime().after(now());
    }

    public boolean isPast() {
        return getEndTime().before(now());
    }

    public boolean isCurrent() {
        Date now = now();
        return getStartTime().before(now) && getEndTime().after(now);
    }
    
    public Optional<Question> getQuestionByProblemId(int problemId) {
        return findFirst(questions, q -> q.getProblemId() == problemId);
    }
}