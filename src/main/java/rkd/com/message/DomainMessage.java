package rkd.com.message;

public class DomainMessage {
    public static final String DOMAIN_NOT_FOUND = "Domínio não encontrado.";
    public static final String DOMAIN_CANNOT_BE_ITS_OWN_PARENT = "O domínio não pode ser pai dele mesmo.";
    public static final String PARENT_DOMAIN_NOT_FOUND = "Domínio pai não encontrado.";
    public static final String CYCLIC_DOMAIN = "A alteração criaria um ciclo na hierarquia de domínios.";
    public static final String UNABLE_TO_DELETE_THE_DOMAIN = "Não é possível excluir um domínio que possui subdomínios.";
}
