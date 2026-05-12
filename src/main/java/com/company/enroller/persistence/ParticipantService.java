package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

    public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}


	public Collection<Participant> getAll(String login, String sortMode, String sortOrder) {
		String hql = "FROM Participant WHERE login LIKE :login";

        if (sortMode.equals("login")){
            hql += " ORDER BY login" ;
            if (sortOrder.equals("ASC") || sortOrder.equals("DESC")){
                hql += " " + sortOrder;
            }
        }

		Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);
        query.setParameter("login", "%"+login+"%");
		return query.list();
	}

    public Participant findByLogin(String login) {
        return (Participant) connector.getSession().get(Participant.class, login);

    }

    public void add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().update(participant);
        transaction.commit();
    }


    public Collection<Participant> getAll() {
        String hql = "FROM Participant";
        Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);
        return query.list();

    }
}
