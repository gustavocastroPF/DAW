/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsul.dao;

import br.edu.ifsul.jpa.EntityManagerUtil;
import br.edu.ifsul.modelo.Estado;
import br.edu.ifsul.util.Util;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 *
 * @author gustavo
 */
public class EstadoDAO {

    private Estado objetoSelecionado;
    private String mensagem = "";
    private EntityManager em;

    public Estado getObjetoSelecionado() {
        return objetoSelecionado;
    }

    public void setObjetoSelecionado(Estado objetoSelecionado) {
        this.objetoSelecionado = objetoSelecionado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EstadoDAO() {
        em = EntityManagerUtil.getEntityManager();
    }

    public boolean validaObjeto(Estado obj) {
        Validator validador = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Estado>> erros = validador.validate(obj);

        if (erros.size() > 0) {
            mensagem = "";
            mensagem += "Objeto com erros:<br/>";
            for (ConstraintViolation<Estado> erro : erros) {
                mensagem += "Erro: " + erro.getMessage() + "<br/>";
            }
            return false;
        } else {
            return true;
        }

    }

    public List<Estado> getLista() {
        return em.createQuery("from Estado order by nome").getResultList();
    }

    public boolean salvar(Estado obj) {
        try {
            em.getTransaction().begin();
            if (obj.getId() == null) {
                em.persist(obj);
            } else {
                em.merge(obj);
            }
            em.getTransaction().commit();
            mensagem = "Objeto persistido";
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive() == false) {
                em.getTransaction().begin();
            }
            em.getTransaction().rollback();
            mensagem = "Erro ao persistir objeto: " + Util.getMensagemErro(e);
            return false;
        }
    }
    
    public boolean remover(Estado obj) {
        try {
            em.getTransaction().begin();
            em.remove(obj);
            em.getTransaction().commit();
            mensagem = "Objeto removido";
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive() == false) {
                em.getTransaction().begin();
            }
            em.getTransaction().rollback();
            mensagem = "Erro ao remover objeto: " + Util.getMensagemErro(e);
            return false;
        }
    }
    
    public Estado localizar(Integer id){
        return em.find(Estado.class, id);
    }

}
