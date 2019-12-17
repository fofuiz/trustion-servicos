package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.CategoriaConverter;
import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.dto.CategoriaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Categoria;
import br.com.accesstage.trustion.repository.criteria.CategoriaSpecification;
import br.com.accesstage.trustion.repository.interfaces.ICategoriaRepository;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.ICategoriaService;
import br.com.accesstage.trustion.util.Mensagem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Override
    public CategoriaDTO criar(CategoriaDTO categoriaDTO) throws Exception {
        Categoria categoria = CategoriaConverter.paraEntidade(categoriaDTO);
        categoria.setDataCriacao(Calendar.getInstance().getTime());

        CategoriaDTO categoriaCriada = CategoriaConverter.paraDTO(categoriaRepository.save(categoria));

        auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{"Tipo Servico", categoriaCriada.getDescricao()}), null);

        return categoriaCriada;
    }

    @Override
    public CategoriaDTO alterar(CategoriaDTO categoriaDTO) throws Exception {
        Categoria categoria = categoriaRepository.findOne(categoriaDTO.getIdCategoria());
        categoriaDTO.setDataCriacao(Calendar.getInstance().getTime());

        BeanUtils.copyProperties(categoriaDTO, categoria);
        categoria.setIdUsuarioAlteracao(categoriaDTO.getIdUsuarioAlteracao());
        categoria.setDataAlteracao(Calendar.getInstance().getTime());

        CategoriaDTO categoriaAlterada = CategoriaConverter.paraDTO(categoriaRepository.save(categoria));
        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{"Categoria", categoriaAlterada.getDescricao()}), null);

        return categoriaAlterada;
    }

    @Override
    public CategoriaDTO pesquisar(Integer idCategoriaDTO) throws Exception {
        Categoria categoria = categoriaRepository.findOne(idCategoriaDTO);
        CategoriaDTO categoriaDTO = CategoriaConverter.paraDTO(categoria);
        return categoriaDTO;
    }

    @Override
    public boolean excluir(Integer idCategoriaDTO) throws Exception {
        if (categoriaRepository.exists(idCategoriaDTO)) {
            categoriaRepository.delete(idCategoriaDTO);
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Categoria"}));
        }

        return true;
    }

    @Override
    public List<CategoriaDTO> listarCriterios(CategoriaDTO categoriaDTO) throws Exception {

        List<CategoriaDTO> categoriasDTO = new ArrayList<>();

        Specification<Categoria> specs = CategoriaSpecification.byCriterio(categoriaDTO);

        for (Categoria categoria : categoriaRepository.findAll(specs)) {
            CategoriaDTO categoriaDTOAux = CategoriaConverter.paraDTO(categoria);
            categoriasDTO.add(categoriaDTOAux);
        }

        return categoriasDTO;

    }

    @Override
    public Page<CategoriaDTO> listarCriterios(CategoriaDTO categoriaDTO, Pageable pageable) throws Exception {
        Page<CategoriaDTO> categoriasDTO;

        Specification<Categoria> specs = CategoriaSpecification.byCriterio(categoriaDTO);

        categoriasDTO = categoriaRepository.findAll(specs, pageable).map((Categoria categoria) -> {
            CategoriaDTO categoriaDTOAux = CategoriaConverter.paraDTO(categoria);
            return categoriaDTOAux;
        });
        return categoriasDTO;

    }

}
