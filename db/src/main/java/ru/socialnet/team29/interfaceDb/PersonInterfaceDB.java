package ru.socialnet.team29.interfaceDb;

import ru.socialnet.team29.model.Person;

public interface PersonInterfaceDB
{

   Person getPersonByEmail(String email);

   String findEmailByPersonId(String id);
}
