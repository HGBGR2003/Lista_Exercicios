
let G = {};

function newGame() {
  const banner = document.getElementById("result-banner");
  if (banner) { banner.style.display = "none"; banner.className = "result-banner"; }
  ["modal-wumpus", "modal-pit"].forEach(function(id) {
    const el = document.getElementById(id);
    if (el) el.style.display = "none";
  });

  G = {
    map: createMap(),
    score: 0,
    arrows: CFG.N_ARROWS,
    goldHeld: 0,
    gameOver: false,
    won: false,
    agent: { row: CFG.SIZE - 1, col: 0, dir: 0 },
    visited: new Set([key(CFG.SIZE - 1, 0)]),
    percs: {
      stench: false,
      breeze: false,
      rustle: false,
      glitter: false,
      bump: false,
      scream: false,
    },
    log: [],
  };

  updatePerceptions();
  buildLabels();
  render();
  log("🎲 Novo jogo iniciado! Explore a caverna com cuidado.", "warn");
}

function addScore(delta) {
  G.score += delta;
}
function log(m, t = "") {
  G.log.unshift({ m, t });
  if (G.log.length > 60) G.log.pop();
}
function dispPos(r, c) {
  return "[" + (c + 1) + "," + (CFG.SIZE - r) + "]";
}
