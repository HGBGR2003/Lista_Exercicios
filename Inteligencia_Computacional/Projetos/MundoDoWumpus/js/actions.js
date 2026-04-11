function action(type) {
  if (G.gameOver) return;

  G.percs.bump = false;
  G.percs.scream = false;

  switch (type) {
    case "forward":
      actionForward();
      break;
    case "turnRight":
      actionTurnRight();
      break;
    case "turnLeft":
      actionTurnLeft();
      break;
    case "shoot":
      actionShoot();
      break;
    case "grab":
      actionGrab();
      break;
    case "climb":
      actionClimb();
      break;
  }

  if (!G.gameOver) {
    updatePerceptions();
    render();
  }
}

function actionForward() {
  addScore(CFG.COST_ACTION);
  const d = DIRS[G.agent.dir];
  const nr = G.agent.row + d.dr;
  const nc = G.agent.col + d.dc;

  if (nr < 0 || nr >= CFG.SIZE || nc < 0 || nc >= CFG.SIZE) {
    G.percs.bump = true;
    log("🧱 Bum! Você bateu em uma parede.", "warn");
    return;
  }

  G.agent.row = nr;
  G.agent.col = nc;
  G.visited.add(key(nr, nc));
  log("⬆️ Moveu para " + dispPos(nr, nc));
  checkRoom();
}

function actionTurnRight() {
  addScore(CFG.COST_ACTION);
  G.agent.dir = (G.agent.dir + 3) % 4;
  log("↩️ Virou à direita → " + DIRS[G.agent.dir].name);
}

function actionTurnLeft() {
  addScore(CFG.COST_ACTION);
  G.agent.dir = (G.agent.dir + 1) % 4;
  log("↪️ Virou à esquerda → " + DIRS[G.agent.dir].name);
}

function actionShoot() {
  if (G.arrows <= 0) {
    log("🏹 Sem flechas disponíveis!", "warn");
    return;
  }

  addScore(CFG.COST_ACTION + CFG.COST_SHOOT);
  G.arrows--;
  log("🏹 Flecha disparada para " + DIRS[G.agent.dir].name + "!", "warn");

  const d = DIRS[G.agent.dir];
  let r = G.agent.row + d.dr;
  let c = G.agent.col + d.dc;
  let hit = false;

  while (r >= 0 && r < CFG.SIZE && c >= 0 && c < CFG.SIZE) {
    const cell = G.map[r][c];
    if (cell.wumpus && cell.wumpusAlive) {
      cell.wumpusAlive = false;
      cell.wumpus = false; // remove do mapa (simplificação visual)
      G.percs.scream = true;
      log("😱 GRITO! Você matou um Wumpus em " + dispPos(r, c) + "!", "good");
      hit = true;
      break;
    }
    r += d.dr;
    c += d.dc;
  }

  if (!hit) log("🏹 A flecha se perdeu na escuridão...", "warn");
}

function actionGrab() {
  addScore(CFG.COST_ACTION);
  const { row: r, col: c } = G.agent;

  if (G.map[r][c].gold) {
    G.map[r][c].gold = false;
    G.goldHeld++;
    addScore(CFG.REW_GOLD);
    log(
      "💰 OURO COLETADO! +" +
        CFG.REW_GOLD +
        " pts (total: " +
        G.goldHeld +
        " pepita(s))",
      "good",
    );
  } else {
    log("💰 Nada para pegar aqui.", "warn");
  }
}

function actionClimb() {
  addScore(CFG.COST_ACTION);
  const { row: r, col: c } = G.agent;

  if (r === CFG.SIZE - 1 && c === 0) {
    G.gameOver = true;
    G.won = true;
    log("🚪 Você saiu da caverna! Pontuação final: " + G.score, "good");
    showOverlay(true, "Você escapou com " + G.goldHeld + " pepita(s) de ouro!");
    render();
  } else {
    log(
      "🚪 A saída fica em [1,1]. Você está em " + dispPos(r, c) + ".",
      "warn",
    );
  }
}

function checkRoom() {
  const { row: r, col: c } = G.agent;
  const cell = G.map[r][c];

  if (cell.pit) {
    addScore(CFG.PEN_PIT);
    log("🕳️ Você caiu em um POÇO! " + CFG.PEN_PIT + " pontos.", "bad");
    G.gameOver = true;
    showOverlay(false, "Você caiu em um poço sem fundo!");
    render();
    return;
  }

  if (cell.wumpus && cell.wumpusAlive) {
    addScore(CFG.PEN_WUMPUS);
    log("👹 O WUMPUS te DEVOROU! " + CFG.PEN_WUMPUS + " pontos.", "bad");
    G.gameOver = true;
    showOverlay(false, "O terrível Wumpus te devorou!");
    render();
    return;
  }

  if (cell.bat) {
    log("🦇 Um morcego gigante te agarrou! Teleportando...", "warn");
    let nr, nc;
    do {
      nr = Math.floor(Math.random() * CFG.SIZE);
      nc = Math.floor(Math.random() * CFG.SIZE);
    } while (nr === r && nc === c);

    G.agent.row = nr;
    G.agent.col = nc;
    G.visited.add(key(nr, nc));
    log("🦇 Teleportado para " + dispPos(nr, nc) + "!", "warn");
    checkRoom(); 
  }
}
