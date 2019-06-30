import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class MyJiraClient {

    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String JIRA_URL = "http://jira.tel-ran.net";


    private String username;
    private String password;
    private String jiraUrl;
    private JiraRestClient restClient;

    private MyJiraClient(String username, String password, String jiraUrl) {
        this.username = username;
        this.password = password;
        this.jiraUrl = jiraUrl;
        this.restClient = getJiraRestClient();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        JiraIssue issue = new JiraIssue();
        do {
            createIssue(issue);
            System.out.println("Do you want to continue?(y/n)");
            String answer = reader.readLine();
            if (answer.equals("n") || answer.equals("N")) {
                break;
            }
        } while (true);
    }

    private static void createIssue(JiraIssue issue) {
        MyJiraClient myJiraClient = new MyJiraClient(USER_NAME, PASSWORD, JIRA_URL);

        for (Projects project : Projects.values()) {
            String issueKey = myJiraClient.createIssue(project.getProjectKey(), issue.getIssueType(), issue.getIssueSummary(), issue.getIssueDescription());
            System.out.println(issueKey);
        }
    }

    private static void inputData(JiraIssue issue, BufferedReader reader) throws IOException {


        inputType(reader, issue);
        inputSummary(reader, issue);
        inputDescription(reader, issue);

    }

    private static void inputDescription(BufferedReader reader, JiraIssue issue) throws IOException {
        System.out.println("Input description of issue");
        issue.setIssueDescription(reader.readLine());
    }

    private static void inputSummary(BufferedReader reader, JiraIssue issue) throws IOException {
        System.out.println("Input title (summary) of issue");
        issue.setIssueSummary(reader.readLine());
    }

    private static void inputType(BufferedReader reader, JiraIssue issue) throws IOException {
        System.out.println("Input type of issue");
        for (IssueType type : IssueType.values()) {
            System.out.println(IssueType.getById(type.getTypeId()));
        }
        issue.setIssueType(IssueType.valueOf(reader.readLine()).getTypeId());
    }

    private String createIssue(String projectKey, Long issueType, String issueSummary, String issueDescription) {

        IssueRestClient issueClient = restClient.getIssueClient();

        IssueInput newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary).setDescription(issueDescription).build();

        return issueClient.createIssue(newIssue).claim().getKey();
    }


    private JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }
}
