document.addEventListener("keydown", (e) => {
  if (G.gameOver) return;
  const keyMap = {
    ArrowUp: "forward",
    ArrowRight: "turnRight",
    ArrowLeft: "turnLeft",
    g: "grab",
    G: "grab",
    a: "shoot",
    A: "shoot",
    s: "climb",
    S: "climb",
  };
  const act = keyMap[e.key];
  if (act) {
    if (e.key.startsWith("Arrow")) e.preventDefault();
    action(act);
  }
});

newGame();
