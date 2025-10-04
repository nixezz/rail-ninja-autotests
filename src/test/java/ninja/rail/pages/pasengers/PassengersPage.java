package ninja.rail.pages.pasengers;

public interface PassengersPage {
    String getHeader();
    PassengersPage enterNameAsInPassport(String name);
    PassengersPage enterPassportNumber(String passportNumber);
    PassengersPage selectFirstCitizenshipInLists();
    PassengersPage enterGender(GenderEnum gender);
    PassengersPage enterBirthdayDate(String dayOfBirth);
    String getAgeNotify();
}
