package ninja.rail.pages.timetable;

import ninja.rail.pages.pasengers.PassengersPage;

public interface TimetablePage {
    TimetablePage selectFirstTrain();
    PassengersPage clickButtonToPassengerInfo();
}
