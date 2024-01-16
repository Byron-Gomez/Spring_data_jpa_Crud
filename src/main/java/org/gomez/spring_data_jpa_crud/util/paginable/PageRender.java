package org.gomez.spring_data_jpa_crud.util.paginable;

import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;

// Clase para facilitar la representación de la información de paginación
public class PageRender<T> {
    private String url;                // URL base para la paginación
    private Page<T> page;               // Objeto Page que contiene la información de la página actual
    private int totalPaginas;           // Número total de páginas
    private int numElementosPorPagina;  // Número de elementos por página
    private int paginaActual;           // Número de la página actual
    private List<PageItem> paginas;     // Lista de objetos PageItem que representan las páginas disponibles

    // Constructor que recibe la URL base y el objeto Page
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<PageItem>();

        // Configuración de parámetros para la paginación
        numElementosPorPagina = 6;
        totalPaginas = page.getTotalPages();
        paginaActual = page.getNumber() + 1;

        int desde, hasta;

        // Lógica para determinar el rango de páginas a mostrar en la paginación
        if (totalPaginas <= numElementosPorPagina) {
            desde = 1;
            hasta = totalPaginas;
        } else {
            if (paginaActual <= numElementosPorPagina / 2) {
                desde = 1;
                hasta = numElementosPorPagina;
            } else if (paginaActual >= totalPaginas - numElementosPorPagina / 2) {
                desde = totalPaginas - numElementosPorPagina + 1;
                hasta = numElementosPorPagina;
            } else {
                desde = paginaActual - numElementosPorPagina / 2;
                hasta = numElementosPorPagina;
            }
        }

        // Generación de objetos PageItem para cada página en el rango
        for (int i = 0; i < hasta; i++) {
            paginas.add(new PageItem(desde + i, paginaActual == desde + i));
        }
    }

    // Métodos getters para obtener información sobre la paginación
    public String getUrl() {
        return url;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    // Métodos adicionales para verificar la posición actual y la existencia de páginas anteriores y siguientes
    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevious() {
        return page.hasPrevious();
    }
}
