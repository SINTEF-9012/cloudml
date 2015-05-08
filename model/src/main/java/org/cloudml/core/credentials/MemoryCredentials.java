package org.cloudml.core.credentials;

/**
 * Created by ferrynico on 08/05/15.
 */
public class MemoryCredentials implements Credentials {

    private String login;
    private String password;


    public MemoryCredentials(String login,String password){
        this.login=login;
        this.password=password;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return login+":"+password;
    }
}
