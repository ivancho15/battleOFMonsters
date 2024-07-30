package co.fullstacklabs.battlemonsters.challenge.service.impl;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class BattleExtendedService extends BattleServiceImpl {
    public BattleExtendedService(BattleRepository battleRepository, ModelMapper modelMapper,
            MonsterRepository monsterRepository) {
        super(battleRepository, modelMapper);
    }
    
    @Autowired 
    private BattleRepository battleRepository;
    
    @Autowired 
    private ModelMapper modelMapper;

    public BattleDTO startBattle(BattleDTO startBattleParamter) {
       
    	MonsterDTO monsterWinner = this.fight(startBattleParamter.getMonsterA(), startBattleParamter.getMonsterB());
    	startBattleParamter.setWinner(monsterWinner);
    	
    	Battle battle = modelMapper.map(startBattleParamter, Battle.class);
    	battle = battleRepository.save(battle);
    	return modelMapper.map(battle, BattleDTO.class);
    }

    public boolean delete(int identifierBattleTodelete) {
        Optional<Battle> battle = battleRepository.findById(identifierBattleTodelete);
        
        if (battle.isPresent()) {
        	battleRepository.deleteById(identifierBattleTodelete);
        } else {
        	throw new ResourceNotFoundException("Battle not found");
        }
        return true;
    }
    
    private MonsterDTO fight(MonsterDTO monsterA, MonsterDTO monsterB ) {
    	MonsterDTO monsterFirst;
    	MonsterDTO monsterSecond;
    	MonsterDTO winer = new MonsterDTO();
    	
    	if (monsterA.getSpeed() != monsterB.getSpeed()) {
    		if (monsterA.getSpeed() > monsterB.getSpeed()) { 
    			monsterFirst = monsterA;
    			monsterSecond = monsterB;
    		} else {
    			monsterFirst = monsterB;
    			monsterSecond = monsterA;
    		}
    	} else {
    		if (monsterA.getAttack() > monsterB.getAttack()) { 
    			monsterFirst = monsterA;
    			monsterSecond = monsterB;
    		} else {
    			monsterFirst = monsterB;
    			monsterSecond = monsterA;
    		}
    	}
    	
    	int turn = 1;
    	int hpAfterDamage;
    	int monsterFirstHP = monsterFirst.getHp();
    	int monsterSecondHP = monsterSecond.getHp();
    	
    	while (monsterFirstHP > 0 && monsterSecondHP > 0) {
    		if (turn % 2 == 0) {
    			hpAfterDamage = monsterFirstHP - this.damage(monsterSecond.getAttack(), monsterFirst.getDefense());
    			monsterFirstHP = hpAfterDamage;
    		} else {
    			hpAfterDamage = monsterSecondHP - this.damage(monsterFirst.getAttack(), monsterSecond.getDefense());
    			monsterSecondHP = hpAfterDamage;
    		}
     		
    		turn++;
    	}   	
    	
    	winer.setId(monsterFirstHP <= 0? monsterSecond.getId() :monsterFirst.getId() );
		winer.setName(monsterFirstHP <= 0? monsterSecond.getName() :monsterFirst.getName() );
		winer.setHp(monsterFirstHP <= 0? monsterSecond.getHp() :monsterFirst.getHp() );
		winer.setAttack(monsterFirstHP <= 0? monsterSecond.getAttack() :monsterFirst.getAttack() );
		winer.setDefense(monsterFirstHP <= 0? monsterSecond.getDefense() :monsterFirst.getDefense() );
		winer.setSpeed(monsterFirstHP <= 0? monsterSecond.getSpeed() :monsterFirst.getSpeed() );
		winer.setImageUrl(monsterFirstHP <= 0? monsterSecond.getImageUrl() :monsterFirst.getImageUrl() );
    	
		return winer;
    }
    
    private int damage (int attack, int defense) {
    	if (attack <= defense) {
    		return 1;
    	} else {
    		return attack - defense;
    	}
    }
}
