package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by codedrinker on 2019/5/23.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean checkUser(String username, String password){
        return userMapper.getByUsernameAndPassword(username, password) != null;
    }

    public User getUsername(String username){
        return userMapper.getByUsername(username);
    }
    public boolean checkUserName(String username){
        return userMapper.getByUsername(username) != null;
    }
    public void updateInfo(User user){
        userMapper.updateInfoById(user);
    }
    public void updatePassword(User user){
        userMapper.updatePasswordById(user);
    }
    public void updateAvatarUrl(User user){
        userMapper.updateAvatarUrlById(user);
    }
    public void updateToken(String accountid,String token){
        userMapper.updateTokenByAccountId(accountid,token);
    }
    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId())
                .andTypeEqualTo(user.getType());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.create(user);
        } else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setEmail(user.getEmail());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setType(user.getType());
            updateUser.setPassword(user.getPassword());
            updateUser.setAccountId(user.getAccountId());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }
}
