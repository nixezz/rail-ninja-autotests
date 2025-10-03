package ninja.rail.pages.pasengers;

public interface PassengersPage {
    String getHeader();
    PassengersPage enterNameAsInPassport(String name);
    PassengersPage enterPassportNumber(String passportNumber);
    PassengersPage selectFirstCitizenshipInLists();
    PassengersPage enterGender(String gender);
    PassengersPage enterBirthdayDate(String birthdayDate);
    String getAgeNotify();
}
