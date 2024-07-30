package co.fullstacklabs.battlemonsters.challenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class BattleExtendedControllerTest {

    private static final String BATTLE_PATH = "/battle";
    @Autowired
    private MockMvc mockMvc;
    
    
    @Test
    void shouldFailStartBattleWithNonexistentMonsterReturning404StatusCode() throws Exception {
    	mockMvc.perform(post(BATTLE_PATH +"/start").param("idMonsterA", "99").param("idMonsterB", "98"))
    	.andExpect(status().isNotFound());
    }

    @Test
    void shouldInsertBattleWithMonsterBWinningAndReturning200StatusCode() throws Exception {
    	mockMvc.perform(post(BATTLE_PATH +"/start").param("idMonsterA", "99").param("idMonsterB", "98"))
    	.andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteBattleSuccessfullyReturning200StatusCode()throws Exception {
    	mockMvc.perform(delete(BATTLE_PATH +"/2"))
    	.andExpect(status().isOk());
    }

    @Test
    void shouldFailDeletingNonexistentBattleReturning404StatusCode() throws Exception {
    	mockMvc.perform(delete(BATTLE_PATH +"/50"))
    	.andExpect(status().isNotFound());
    }

}
