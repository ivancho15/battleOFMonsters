package co.fullstacklabs.battlemonsters.challenge.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.hibernate.exception.GenericJDBCException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterExtendedService;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;

@ExtendWith(MockitoExtension.class)
public class MonsterExtendedServiceTest {
    @InjectMocks
    public MonsterExtendedService monsterService;

    @Mock
    public MonsterRepository monsterRepository;

    @Mock
    private ModelMapper mapper;
	
    @Mock
    MultipartFile multipartFileReader;
	
    @Test
    public void testDeleteMonsterSuccessfully() {
        
    	int id= 1;
    	Monster monster = MonsterTestBuilder.builder().id(id)
    			.name("monster Name 1").build();
    	
    	Optional<Monster> monsteOp = Optional.of(monster);
    	Mockito.when(monsterRepository.findById(id)).thenReturn(monsteOp);
    	assertDoesNotThrow(() -> { monsterService.delete(id);});
    }

     @Test
     void testImportCsvSucessfully() throws Exception {
         final byte[] data = Files.readAllBytes(Paths.get("data/monsters-correct.csv"));
         InputStream imput = new ByteArrayInputStream(data);
         assertDoesNotThrow(() -> {monsterService.importFromInputStream(imput);});
     }


    @Test
    @ExceptionHandler
    public void whenImportFromInputStreamThrowsIOException_thenUnprocessableFileExceptionIsThrown()  throws Exception {
    	final byte[] data = Files.readAllBytes(Paths.get("data/monsters-wrong-column.csv"));
        InputStream imput = new ByteArrayInputStream(data);
        
        //assertThrows(GenericJDBCException.class, () -> {monsterService.importFromInputStream(imput);} );
        //assertThrows(IOException.class, () -> {monsterService.importFromInputStream(imput);} );
        assertThrows(UnprocessableFileException.class, () -> {monsterService.importFromInputStream(imput);} );
    }

}
