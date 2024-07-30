package co.fullstacklabs.battlemonsters.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;


@RestController
@RequestMapping("/battle")
public class BattleExtendedController {

    @Autowired
    private BattleService battleService;
    
    @Autowired
    private MonsterService monsterService;
	
	@PostMapping("/start")
    public ResponseEntity<BattleDTO> startBattle(@RequestParam(value="idMonsterA") String id_monsterA, @RequestParam(value="idMonsterB") String id_monsterB) {
        
		MonsterDTO monsterA = monsterService.findById(Integer.parseInt(id_monsterA));
		MonsterDTO monsterB = monsterService.findById(Integer.parseInt(id_monsterB));
		
		BattleDTO battleStarted = new BattleDTO();
		
		battleStarted.setMonsterA(monsterA);
		battleStarted.setMonsterB(monsterB);
		
		return new ResponseEntity<>(battleService.startBattle(battleStarted), HttpStatus.CREATED);		
		
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id)  {
        return this.battleService.delete(Integer.parseInt(id));
    }

}
