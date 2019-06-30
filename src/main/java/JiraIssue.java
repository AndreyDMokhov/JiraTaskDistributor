import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class JiraIssue {

    private String issueKey;
    private Long issueType;
    private String issueSummary;
    private String issueDescription;

}
