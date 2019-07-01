import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class MyJiraClient {

    private static final String USER_NAME = "andreydmokhov";
    private static final String PASSWORD = "Oksana76";
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
        MyJiraClient myJiraClient = new MyJiraClient(USER_NAME, PASSWORD, JIRA_URL);
        JiraIssue issue = new JiraIssue();
        do {
            inputData(issue, reader);
            if (issue.getIssueType().equals(IssueType.EPIC.getTypeId())) {
                sendingIssueEpic(myJiraClient, issue);
            } else {
                sendingIssueTask(myJiraClient, issue);
            }

            System.out.println("Do you want to continue?(y/n)");
            String answer = reader.readLine();
            if (answer.equals("n") || answer.equals("N")) {
                break;
            }
        } while (true);
    }


    private static void sendingIssueTask(MyJiraClient myJiraClient, JiraIssue issue) {
        for (Projects project : Projects.values()) {

            String issueKey = myJiraClient
                    .createIssue(project.getProjectKey(), issue);
            System.out.println(issueKey);
        }

    }

    private static void sendingIssueEpic(MyJiraClient myJiraClient, JiraIssue issue) {
        for (Projects project : Projects.values()) {

            String issueKey = myJiraClient
                    .createIssueEpic(project.getProjectKey(), issue);
            System.out.println(issueKey);
        }

    }

    private static void inputData(JiraIssue issue, BufferedReader reader) throws IOException {

        inputType(reader, issue);
        if (issue.getIssueType().equals(IssueType.EPIC.getTypeId())) {
            inputEpicName(reader, issue);
        }
        inputSummary(reader, issue);
        inputDescription(reader, issue);

    }

    private static void inputEpicName(BufferedReader reader, JiraIssue issue) throws IOException {
        System.out.println("Input epic name of issue");
        issue.setEpicName(reader.readLine());
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

    private String createIssue(String projectKey, JiraIssue issue) {

        IssueRestClient issueClient = restClient.getIssueClient();
        IssueInput newIssue = new IssueInputBuilder(projectKey, issue.getIssueType(), issue.getIssueSummary())
                .setDescription(issue.getIssueDescription())
                .build();
        return issueClient.createIssue(newIssue).claim().getKey();
    }

    private String createIssueEpic(String projectKey, JiraIssue issue) {

        IssueRestClient issueClient = restClient.getIssueClient();

        IssueInput newIssue = new IssueInputBuilder(projectKey, issue.getIssueType(), issue.getIssueSummary())
                .setDescription(issue.getIssueDescription())
                .setFieldInput(new FieldInput("customfield_10114", issue.getEpicName()))
                .build();

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
