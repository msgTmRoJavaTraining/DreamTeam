package group.msg.exceptions;

public class UserCreatorException extends RuntimeException{
    public UserCreatorException(String lastName,String firstName) {
        super("User "+lastName+" "+firstName+"can't be created!!");
    }
}
