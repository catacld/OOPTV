package classes;

public class Credentials {
    private String name;
    private  String password;
    private String accountType;
    private String country;
    private String balance;

    public Credentials() {
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getPassword() {
        return this.password;
    }

    public final void setPassword(final String password) {
        this.password = password;
    }

    public final String getAccountType() {
        return this.accountType;
    }

    public final void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    public final String getCountry() {
        return this.country;
    }

    public final void setCountry(final String country) {
        this.country = country;
    }

    public final String getBalance() {
        return this.balance;
    }

    public final void setBalance(final String balance) {
        this.balance = balance;
    }


}

