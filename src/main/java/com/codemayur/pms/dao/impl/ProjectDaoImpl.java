package com.codemayur.pms.dao.impl;

import java.util.List;
import java.util.Objects;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.codemayur.pms.entity.Project;

@Repository
public class ProjectDaoImpl {

	SessionFactory sessionFactory;

	@Autowired
	public ProjectDaoImpl(
			SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<Project> getAllProjects() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			Query<Project> query = session.createQuery("FROM project");

			transaction.commit();
			return query.list();
		} catch (Exception e) {
			if (Objects.nonNull(transaction)) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Project getProjectByProjectId(
			int projectId) {
		List<Project> list;
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			Query<Project> query = session.createQuery("FROM project WHERE id=:id");
			query.setParameter("id",
					projectId);

			list = query.list();

			transaction.commit();
		} catch (Exception e) {
			if (Objects.nonNull(transaction)) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public void postProject(
			Project project) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			session.save(project);

			transaction.commit();
		} catch (Exception e) {
			if (Objects.nonNull(transaction)) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Integer getMaxProjectId() {
		List<Integer> list;
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			Query<Integer> query = session.createQuery("SELECT MAX(id) FROM project");

			transaction.commit();
			list = query.list();
		} catch (Exception e) {
			if (Objects.nonNull(transaction)) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public boolean isDuplicateProjectNamePresent(
			String projectName) {
		boolean isDuplicateProjectNamePresent = false;
		List<Integer> list;
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			Query<Integer> query = session.createQuery("FROM project WHERE name=:name");
			query.setParameter("name", projectName);
			
			transaction.commit();
			list = query.list();
		} catch (Exception e) {
			if (Objects.nonNull(transaction)) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}

		if (list != null && !list.isEmpty()) {
			isDuplicateProjectNamePresent = true;
		}
		return isDuplicateProjectNamePresent;
	}

}
