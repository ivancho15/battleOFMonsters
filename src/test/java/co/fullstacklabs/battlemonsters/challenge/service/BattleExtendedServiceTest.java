package co.fullstacklabs.battlemonsters.challenge.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleExtendedService;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;


@ExtendWith(MockitoExtension.class)
public class BattleExtendedServiceTest {
	   
    @InjectMocks
    public BattleExtendedService battleService;

    @Mock
    public BattleRepository battleRepository;

    @Mock
    private ModelMapper mapper;

	
    @Test
    void shouldInsertBattleWithMonsterBWinning() {
    	MonsterDTO monsterA = MonsterDTO.builder().id(5).name("Monster A")
    		.attack(50).defense(20).hp(30).speed(10).build();
    	MonsterDTO monsterB = MonsterDTO.builder().id(4).name("Monster B")
			.attack(80).defense(40).hp(80).speed(30).build();
    	
    	BattleDTO battleA = BattleDTO.builder().id(99).monsterA(monsterA)
    			.monsterB(monsterB).build();
    	
    	Battle BattleResult = new Battle();
    	
    	Mockito.when(battleRepository.save(mapper.map(battleA, Battle.class)))
    		.thenReturn(BattleResult);
    	
    	BattleDTO battleCompleted = battleService.startBattle(mapper.map(BattleResult, BattleDTO.class));
    	assertEquals(monsterB, battleCompleted.getWinner());
    }
    
    @Test
    void shouldDeleteBattleSuccessfully() {
        int id=2;
        
        Battle battleDeleted = BattleTestBuilder.builder().id(id).build();
        Optional<Battle> optionaBattle = Optional.of(battleDeleted);
        
        Mockito.when(battleRepository.findById(id)).thenReturn(optionaBattle);
        
        boolean result =  battleService.delete(id);
        
        assertTrue(result);
    }
}
