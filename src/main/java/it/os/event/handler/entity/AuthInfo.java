package it.os.event.handler.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfo {
    
    private String username;

    private String jwtToken;

    private List<String> roles;

    private String authorizations;

    public void setRoleList(String ...role) {

        if(!CollectionUtils.isEmpty(Arrays.asList(role))) {
            roles = Arrays.asList(role);
        } else {
            roles = new ArrayList<>();
        }
    }
}
