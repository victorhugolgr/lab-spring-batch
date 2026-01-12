package br.com.victorhugolgr.lab.jobs.importuser;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import br.com.victorhugolgr.lab.dto.User;

@Component
public class UserItemProcessor implements ItemProcessor<User, User>{

    @Override
    public User process(User user) throws Exception {
        if(user.id() %2 == 0) {
            return user;
        }
        return null;
    }

}
