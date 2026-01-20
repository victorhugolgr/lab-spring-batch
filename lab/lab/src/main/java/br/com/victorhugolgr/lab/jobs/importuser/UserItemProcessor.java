package br.com.victorhugolgr.lab.jobs.importuser;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import br.com.victorhugolgr.lab.dto.UserDTO;

@Component
public class UserItemProcessor implements ItemProcessor<UserDTO, UserDTO>{

    @Override
    public UserDTO process(UserDTO user) throws Exception {
        if(user.getId() %2 == 0) {
            return user;
        }
        return null;
    }

}
