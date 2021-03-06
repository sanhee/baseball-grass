package com.team22.baseball.controller;

import com.team22.baseball.domain.Game;
import com.team22.baseball.dto.request.UpdatePlayerInfo;
import com.team22.baseball.dto.response.DetailScore.DetailScore;
import com.team22.baseball.dto.response.GameList.GameList;
import com.team22.baseball.dto.response.PlayerScoreList.ScoreList;
import com.team22.baseball.dto.response.TeamSelect.NextPlayerInfoDto;
import com.team22.baseball.dto.response.TeamSelect.TeamListDto;
import com.team22.baseball.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ApiGameController {

    private final Logger logger = LoggerFactory.getLogger(ApiGameController.class);

    private final GameService gameService;

    @Autowired
    private ApiGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("game_list")
    private List<GameList> gameList() {
        logger.debug("gameService.findAllGame() : {}", gameService.findAllGame());
        return gameService.findAllGame();
    }

    @GetMapping("select_team/{title}")
    @ResponseStatus(HttpStatus.OK)
    private List<TeamListDto> selectGame(@PathVariable String title) throws Exception {
        title = Objects.toString(title, "");
        gameService.updateGameStatusByTitle(title);

        return gameService.getInfoSelectedTeam(title);
    }

    @PutMapping("/update_player")
    @ResponseStatus(HttpStatus.CREATED)
    private NextPlayerInfoDto updatePlayerInfo(@RequestBody UpdatePlayerInfo req) throws Exception {
        return gameService.updatePlayerInfo(req);
    }

    @GetMapping("/detailScore/{gameID}")
    @ResponseStatus(HttpStatus.OK)
    private List<DetailScore> detailScore(@PathVariable Long gameID) {
        return gameService.getDetailScoreOfEachTeam(gameID);
    }

    @GetMapping("/playerList/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    private List<ScoreList> playerScoreList(@PathVariable Long gameId) throws Exception {
        Game findGame = gameService.findGameById(gameId);
        return gameService.getPlayerScoreOfGame(findGame);
    }

    @PutMapping("/reset")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    private void resetData(@RequestBody Map<String, Long> request) {
       gameService.resetGameData(request.get("teamId"));
    }

}
