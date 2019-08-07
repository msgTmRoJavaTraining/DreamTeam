package group.msg.jsf_beans;

public class UserManagementBean {
    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String mobile;
    private String email;

    public void createUser(){
        StringBuilder generatedUser=new StringBuilder();
        generatedUser.append(lastName.substring(4));
        generatedUser.append(firstName.charAt(0));


    }


}
