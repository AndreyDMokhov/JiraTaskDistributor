import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum IssueType {
    TASK(10003L),
    EPIC(10001L),
    TEST(10000L),
    BUG(10005L),
    STORY(10002L);

    private  Long typeId;

    public static IssueType getById(final Long typeId) {
        return Arrays.stream(values())
                .filter(x -> x.getTypeId().equals(typeId))
                .findFirst()
                .orElse(null);
    }
}
