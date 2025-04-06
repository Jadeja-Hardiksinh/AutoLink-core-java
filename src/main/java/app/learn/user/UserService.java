package app.learn.user;

import app.learn.common.util.HashUtil;

public class UserService {

    public boolean createUser(User user) {
        UserDAO userDAO = new UserDAO();
        return userDAO.add(user);
    }

    public boolean userLogin(UserLoginDTO userLoginDTO) {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByEmail(userLoginDTO.getEmail());
        if (user != null) {
            userLoginDTO.setUserRole(user.getRole());
            userLoginDTO.setId(user.getId());
            return HashUtil.compareHash(userLoginDTO.getPassword(), user.getPassword());
        } else {
            return false;
        }
    }
}
