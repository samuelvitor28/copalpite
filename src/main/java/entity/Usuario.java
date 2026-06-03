package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true)
    private String username;

    @Column (nullable = false)
    private String nome;

    @Column(nullable = false)
    private String ultimoNome;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "selecao_torcida_id")
    private Selecao selecaoTorcida;

    @Column(updatable = false)
    private LocalDateTime criadoEm;
}
