package br.com.accesstage.trustion.util;

import br.com.accesstage.trustion.seguranca.model.UsuarioDetails;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.when;

/**
 *
 * @author raphael
 */
public class TestUtil {

    public TestUtil() {
        // classe para evitar o erro no sonar
    }

    /**
     * Removes the final modifier and replace the field with the mocked one.
     *
     * @param classInstance
     * @param field
     * @param newValue
     */
    public static void setFinalStatic(Object classInstance, Field field, Object newValue) {
        try {
            field.setAccessible(true);
            Field modifiersField;
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(classInstance, newValue);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TestUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TestUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método responsável por criar os objetos e os mocks relacionados a autenticação do usuário utilizado pelo spring
     * security
     * @return UsuarioDetails autenticado
     */
    public static UsuarioDetails inicializaMockAutenticacao() {
        UsuarioDetails usuarioDetails = new UsuarioDetails();
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioDetails);

        return usuarioDetails;
    }

}
