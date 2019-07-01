import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Projects {
   COFFEE_SHOP_COOL_TEAM_KEY("CT"), //ID 10503
    COFFEE_SHOP_JAVA_BEANS_TEAM_KEY("JBT"), //ID 10502
    COFFEE_SHOP_JOYTEAM_KEY("JT"), //ID 10504
    COFFEE_SHOP_UP_AND_GO_KEY("UAG"),//ID 10501
    COFFEE_SHOP_PROJECT_KEY("CSP"); //ID 10310

    private String projectKey;

    public static Projects getById(String projectKey) {
        return Arrays.stream(values())
                .filter(x -> x.getProjectKey().equals(projectKey))
                .findFirst()
                .orElse(null);
    }
}
