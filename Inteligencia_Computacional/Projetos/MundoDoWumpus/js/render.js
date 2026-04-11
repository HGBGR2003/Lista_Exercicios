function render() {
  renderGrid();
  renderHUD();
  renderPerceptions();
  renderLog();
}

function renderGrid() {
  const gridEl = document.getElementById("game-grid");
  gridEl.innerHTML = "";
  const S = CFG.SIZE;
  const { row: ar, col: ac, dir: ad } = G.agent;
  const exitR = S - 1;

  for (let r = 0; r < S; r++) {
    for (let c = 0; c < S; c++) {
      const el = document.createElement("div");
      el.className = "cell";
      const isAgent = r === ar && c === ac;
      const isVisited = G.visited.has(key(r, c));
      const isExit = r === exitR && c === 0;

      if (isAgent) el.classList.add("current");
      else if (isVisited) el.classList.add("visited");
      if (isExit && !isAgent) el.classList.add("exit-cell");

      const coord = document.createElement("div");
      coord.className = "cell-coord";
      coord.textContent = c + 1 + "," + (S - r);
      el.appendChild(coord);

      const icons = document.createElement("div");
      icons.className = "cell-icons";

      if (isAgent) {
        icons.textContent = "🧭" + DIRS[ad].icon;
      } else if (!isVisited) {
        icons.textContent = "❓";
      } else {
        const mc = G.map[r][c];
        let txt = "";
        if (mc.wumpus && mc.wumpusAlive) txt += "👹";
        if (mc.pit) txt += "🕳️";
        if (mc.gold) txt += "💰";
        if (mc.bat) txt += "🦇";
        if (isExit) txt += "🚪";
        icons.textContent = txt || "·";
      }
      el.appendChild(icons);

      if (isVisited && !isAgent) {
        const adj = getAdj(r, c);
        let pTxt = "";
        if (
          adj.some(
            ([ar2, ac2]) =>
              G.map[ar2][ac2].wumpus && G.map[ar2][ac2].wumpusAlive,
          )
        )
          pTxt += "💀";
        if (adj.some(([ar2, ac2]) => G.map[ar2][ac2].pit)) pTxt += "🌬️";
        if (adj.some(([ar2, ac2]) => G.map[ar2][ac2].bat)) pTxt += "🦇";
        if (pTxt) {
          const pEl = document.createElement("div");
          pEl.className = "cell-percs";
          pEl.textContent = pTxt;
          el.appendChild(pEl);
        }
      }

      gridEl.appendChild(el);
    }
  }
}

function renderHUD() {
  const sc = G.score;
  const sdEl = document.getElementById("score-display");
  sdEl.textContent = sc >= 0 ? "+" + sc : sc;
  sdEl.className = "score-big " + (sc >= 0 ? "pos" : "neg");

  document.getElementById("s-arrows").textContent = G.arrows;
  document.getElementById("s-gold").textContent = G.goldHeld;

  const { row, col, dir } = G.agent;
  document.getElementById("s-pos").textContent = dispPos(row, col);
  document.getElementById("s-dir").textContent =
    DIRS[dir].icon + " " + DIRS[dir].name;

  const over = G.gameOver;
  const atExit = row === CFG.SIZE - 1 && col === 0;

  ["btn-forward", "btn-turnRight", "btn-turnLeft", "btn-grab"].forEach(
    (id) => (document.getElementById(id).disabled = over),
  );
  document.getElementById("btn-shoot").disabled = over || G.arrows <= 0;
  document.getElementById("btn-climb").disabled = over || !atExit;
}

function renderPerceptions() {
  document.querySelectorAll(".perc-item").forEach((el) => {
    el.className = "perc-item " + (G.percs[el.dataset.id] ? "active" : "");
  });
}

function renderLog() {
  document.getElementById("action-log").innerHTML = G.log
    .map((e) => `<div class="log-entry ${e.t}">${e.m}</div>`)
    .join("");
}

function buildLabels() {
  const S = CFG.SIZE;
  const topEl = document.getElementById("col-labels");
  const leftEl = document.getElementById("row-labels");
  topEl.innerHTML = leftEl.innerHTML = "";

  for (let c = 0; c < S; c++) {
    const sp = document.createElement("span");
    sp.textContent = c + 1;
    topEl.appendChild(sp);
  }
  for (let r = 0; r < S; r++) {
    const sp = document.createElement("span");
    sp.textContent = S - r;
    leftEl.appendChild(sp);
  }
}

function showOverlay(won, msg) {
  const card = document.getElementById("ov-card");
  card.className = "overlay-card " + (won ? "win" : "lose");
  document.getElementById("ov-emoji").textContent = won ? "🏆" : "💀";
  document.getElementById("ov-title").textContent = won
    ? "Você Venceu!"
    : "Game Over";
  document.getElementById("ov-msg").textContent = msg;
  const sc = G.score;
  document.getElementById("ov-score").textContent = sc >= 0 ? "+" + sc : sc;
  document.getElementById("overlay").classList.add("show");
}
