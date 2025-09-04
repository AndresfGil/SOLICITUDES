package co.com.crediya.pragma.solicitudes.model.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
public class SolicitudPage<T> implements Iterable<T> {

    public SolicitudPage(List<T> data, Long totalRows, Integer pageSize, Integer pageNum) {
        this.data = data;
        this.totalRows = totalRows;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public SolicitudPage(List<T> data, Long totalRows, Integer pageSize, Integer pageNum, Boolean hasNext, String sort) {
        this.data = data;
        this.totalRows = totalRows;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.hasNext = hasNext;
        this.sort = sort;

    }

    List<T> data = new ArrayList<>();
    Long totalRows;
    Integer pageSize;
    Integer pageNum;
    String sort;

    Boolean hasNext;


    public void add(T item) {
        data.add(item);
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }
}