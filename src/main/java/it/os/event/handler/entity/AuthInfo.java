package it.os.event.handler.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfo {
    
    private String username;

    private String jwtToken;

    private List<String> roles;

    public void setRoleList(String ...role) {

        if(CollectionUtils.isEmpty(Arrays.asList(role))) {
            roles = Arrays.asList(role);
        } else {
            roles = new ArrayList<>();
        }
    }
}