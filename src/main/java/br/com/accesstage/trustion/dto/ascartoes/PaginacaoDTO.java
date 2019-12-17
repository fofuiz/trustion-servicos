package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;

/**
 * Classe responsável para passar os parametros de paginação
 *
 * @author Caio Felipe Bispo Moraes
 * @since 02/09/2015
 */
public class PaginacaoDTO implements Serializable {

    private static final long serialVersionUID = 4000110000035638247L;

    private int first;
    private int pageSize;
    private String sortField;
    private boolean ascOrder;

    public PaginacaoDTO() {
    }

    public PaginacaoDTO(int first, int pageSize, String sortField, boolean ascOrder) {
        this.first = first;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.ascOrder = ascOrder;
    }

    /**
     * @return the first
     */
    public int getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the sortField
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * @param sortField the sortField to set
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * @return the ascOrder
     */
    public boolean isAscOrder() {
        return ascOrder;
    }

    /**
     * @param ascOrder the ascOrder to set
     */
    public void setAscOrder(boolean ascOrder) {
        this.ascOrder = ascOrder;
    }

    /**
     * @return número da página
     */
    public Integer getNumeroPagina() {
        float numeroPagina = (first / pageSize + 1);
        return Math.round(numeroPagina);
    }
}
