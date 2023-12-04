package model;

import androidx.annotation.NonNull;

public class User {

    @NonNull
    @Override
    public String toString() {
        return emailAddress;
    }

    private String emailAddress;
    private String languagePrefer;

    public User(String emailAddress, String languagePrefer) {
        this.emailAddress = emailAddress;
        this.languagePrefer = languagePrefer;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLanguagePrefer() {
        return languagePrefer;
    }

    public void setLanguagePrefer(String languagePrefer) {
        this.languagePrefer = languagePrefer;
    }
}
