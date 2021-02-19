package br.com.sisdb.dscatalog.dto;


import br.com.sisdb.dscatalog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    private String password;


    UserInsertDTO(){
        super();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
