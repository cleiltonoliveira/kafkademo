package br.com.kafkademo.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City implements Serializable {
    private String name;
    private String uf;
}
