
package br.com.projetojsf.carros.dao;

import br.com.projetojsf.carros.entidade.Carro;
import br.com.projetojsf.carros.util.FabricaConexao;
import br.com.projetojsf.carros.util.exception.ErroSistema;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Administrador
 */
public class CarroDAO implements CrudDAO<Carro>{
    
    @Override
    public void salvar(Carro carro) throws ErroSistema{
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps;
            if(carro.getId() == null){
                ps = conexao.prepareStatement("INSERT INTO `carro` (`modelo`,`fabricante`,`cor`,`ano`) VALUES (?,?,?,?);");
            }else{
                ps = conexao.prepareStatement("UPDATE carro SET modelo=?, fabricante=?, cor=?, ano=? where id=?");
                ps.setInt(5, carro.getId());
            }
            ps.setString(1, carro.getModelo());
            ps.setString(2, carro.getFabricante());
            ps.setString(3, carro.getCor());
            ps.setDate(4, new Date(carro.getAno().getTime()));
            ps.execute();
            FabricaConexao.fecharConexao();
            
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao tentar Salvar!", ex);
        }
    
    }
    
    public void deletar(Carro carro) throws ErroSistema{
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps = conexao.prepareStatement("DELETE FROM carro WHERE id = ?");
            ps.setInt(1, carro.getId());
            ps.execute();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao deletar o carro!", ex);
        }
    
    }
    
    @Override
    public List<Carro> buscar() throws ErroSistema{
    
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps = conexao.prepareStatement("SELECT * FROM carro");
            ResultSet resultSet = ps.executeQuery();
            List<Carro> carros = new ArrayList<>();
            while(resultSet.next()){
                Carro carro = new Carro();
                carro.setId(resultSet.getInt("id"));
                carro.setModelo(resultSet.getString("modelo"));
                carro.setFabricante(resultSet.getString("fabricante"));
                carro.setCor(resultSet.getString("cor"));
                carro.setAno(resultSet.getDate("ano"));
                carros.add(carro);
            }
            FabricaConexao.fecharConexao();
            return carros;
            
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao buscar os carros!", ex);
        }
        
    }
    
}
