package ru.socialnet.team29.interfaceDb;

import ru.socialnet.team29.model.Person;

import java.util.List;

public interface PersonInterfaceDB
{

   Person getPersonByEmail(String email);

   String findEmailByPersonId(String id);

   List<Integer> getPersonBirthDate();
}
