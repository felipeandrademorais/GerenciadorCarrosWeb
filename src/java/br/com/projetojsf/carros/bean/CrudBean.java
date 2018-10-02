
package br.com.projetojsf.carros.bean;

import br.com.projetojsf.carros.dao.CrudDAO;
import br.com.projetojsf.carros.util.exception.ErroSistema;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


public abstract class CrudBean<E, D extends CrudDAO> {
    
    private String estadoTela = "buscar"; //Inseri, Edita, Busca
    
    private E entidade;
    private List<E> entidades;
    
    public void novo(){
        entidade = criarNovaEntidade();
        mudarParaInseri();
    }
    public void salvar(){
        try {
            getDao().salvar(entidade);
            entidade = criarNovaEntidade();
            adicionarMensagem("Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
            mudarParaBusca();
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    public void editar(E entidade){
        this.entidade = entidade;
        mudarParaEdita();
    }
    public void deletar(E entidade){
        try {
            getDao().deletar(entidade);
            entidades.remove(entidade);
            adicionarMensagem("Deletado com sucesso!", FacesMessage.SEVERITY_INFO);
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    public void buscar(){
        if(isBusca() == false){
            mudarParaBusca();
            return;
        }
        try {
            entidades = getDao().buscar();
            if(entidade == null || entidades.size() < 1){
                adicionarMensagem("Nada Cadastrado", FacesMessage.SEVERITY_WARN);
            }
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    public void adicionarMensagem(String mensagem, FacesMessage.Severity tipoErro){
        FacesMessage fm = new FacesMessage(tipoErro, mensagem, null); 
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }
    
    //getters e setters

    public E getEntidade() {
        return entidade;
    }

    public void setEntidade(E entidade) {
        this.entidade = entidade;
    }

    public List<E> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<E> entidades) {
        this.entidades = entidades;
    }
   
        
    //Cria metodos nas classes bean
    public abstract D getDao();
    public abstract E criarNovaEntidade();  
    
    //Metodos para controle da tela;
    public boolean isInseri(){
        return "inserir".equals(estadoTela);
    } 
    public boolean isEdita(){
        return "editar".equals(estadoTela);
    } 
    public boolean isBusca(){
        return "buscar".equals(estadoTela);
    } 
    public void mudarParaInseri(){
        estadoTela = "inserir";
    }
    public void mudarParaEdita(){
        estadoTela = "editar";
    }
    public void mudarParaBusca(){
        estadoTela = "buscar";
    }
}
