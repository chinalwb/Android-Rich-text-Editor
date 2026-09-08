"""Generates the ARE toolbar icon set as VectorDrawables on a 24x24 grid."""
import os, sys
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from glyphs import fit

HERE = os.path.dirname(os.path.abspath(__file__))
OUT = os.path.join(HERE, '..', '..', 'ARE', 'are', 'src', 'main', 'res', 'drawable')

TEMPLATE = '''<?xml version="1.0" encoding="utf-8"?>
<!-- Part of the ARE toolbar icon set: 24dp grid, 20dp live area, single weight. -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
{paths}</vector>
'''

PATH = '''    <path
        android:fillColor="#FF000000"{fill_type}
        android:pathData="{data}" />
'''

def n(v):
    return ('%.2f' % v).rstrip('0').rstrip('.')

def rect(x, y, w, h):
    return f"M{n(x)},{n(y)}h{n(w)}v{n(h)}h{n(-w)}z"

def rrect(x, y, w, h, r):
    return (f"M{n(x+r)},{n(y)}h{n(w-2*r)}a{n(r)},{n(r)} 0 0 1 {n(r)},{n(r)}"
            f"v{n(h-2*r)}a{n(r)},{n(r)} 0 0 1 {n(-r)},{n(r)}"
            f"h{n(-(w-2*r))}a{n(r)},{n(r)} 0 0 1 {n(-r)},{n(-r)}"
            f"v{n(-(h-2*r))}a{n(r)},{n(r)} 0 0 1 {n(r)},{n(-r)}z")

def circle(cx, cy, r):
    return (f"M{n(cx)},{n(cy-r)}a{n(r)},{n(r)} 0 1 1 0,{n(2*r)}"
            f"a{n(r)},{n(r)} 0 1 1 0,{n(-2*r)}z")

def bars(ys, x, w, h=2.2):
    return "".join(rect(x, y, w, h) for y in ys)

def write(name, paths, even_odd=False):
    body = ""
    for p in paths:
        ft = '\n        android:fillType="evenOdd"' if even_odd else ''
        body += PATH.format(data=p, fill_type=ft)
    with open(os.path.join(OUT, name + '.xml'), 'w') as f:
        f.write(TEMPLATE.format(paths=body))
    return name

os.makedirs(OUT, exist_ok=True)
made = []

# ---------------------------------------------------------------- letterforms
made.append(write('are_ic_bold', [fit('B', 'bold', (4.5, 2.5, 15, 19))]))
made.append(write('are_ic_italic', [fit('I', 'italic', (5, 2.5, 14, 19))]))
made.append(write('are_ic_underline', [
    fit('U', 'bold', (5, 2, 14, 15)),
    rect(4, 19.4, 16, 2.4)]))
made.append(write('are_ic_strikethrough', [
    fit('S', 'bold', (6.5, 3, 11, 18)),
    rect(2, 10.9, 20, 2.4)]))

# text size: two A's sharing a baseline
made.append(write('are_ic_fontsize', [
    fit('A', 'bold', (1.5, 5, 13, 16)),
    fit('A', 'bold', (14.5, 11.4, 8.5, 9.6))]))

# text colour: A over a colour bar
made.append(write('are_ic_foregroundcolor', [
    fit('A', 'bold', (4, 1.5, 16, 14.5)),
    rect(3, 18.6, 18, 3.4)]))

# highlight: A knocked out of a filled block, over a colour bar
made.append(write('are_ic_background', [
    rrect(3, 1.5, 18, 14.5, 2) + fit('A', 'bold', (5.5, 3.5, 13, 10.5)),
    rect(3, 18.6, 18, 3.4)], even_odd=True))

# font family: A in a rounded frame
made.append(write('are_ic_fontface', [
    rrect(2.5, 2.5, 19, 19, 3) + rrect(4.7, 4.7, 14.6, 14.6, 1.6),
    fit('A', 'bold', (6.5, 6.5, 11, 11))], even_odd=True))

made.append(write('are_ic_superscript', [
    fit('X', 'bold', (1.5, 6, 13.5, 15),),
    fit('2', 'bold', (15.5, 2.5, 7, 8))]))
made.append(write('are_ic_subscript', [
    fit('X', 'bold', (1.5, 3, 13.5, 15)),
    fit('2', 'bold', (15.5, 13.5, 7, 8))]))

made.append(write('are_ic_quote', [fit('”', 'black', (3, 3.5, 18, 17))]))
made.append(write('are_ic_at', [fit('@', 'regular', (1.5, 1.5, 21, 21))]))

# ---------------------------------------------------------------- lists
LIST_ROWS = [4.4, 10.9, 17.4]
made.append(write('are_ic_listbullet',
    [circle(4.6, y + 1.1, 1.7) for y in LIST_ROWS]
    + [rect(9.4, y, 12.6, 2.2) for y in LIST_ROWS]))

made.append(write('are_ic_listnumber',
    [fit(d, 'bold', (1.4, y - 1.1, 5.6, 4.6))
     for d, y in zip('123', LIST_ROWS)]
    + [rect(9.4, y, 12.6, 2.2) for y in LIST_ROWS]))

# ---------------------------------------------------------------- alignment
ROWS = [3.4, 8.3, 13.2, 18.1]
def align(kind):
    out = []
    for i, y in enumerate(ROWS):
        full = i % 2 == 0
        w = 18 if full else 11
        if kind == 'left':
            x = 3
        elif kind == 'right':
            x = 21 - w
        else:
            x = 12 - w / 2
        out.append(rect(x, y, w, 2.2))
    return out
made.append(write('are_ic_alignleft', align('left')))
made.append(write('are_ic_aligncenter', align('center')))
made.append(write('are_ic_alignright', align('right')))

# indent: text block plus an arrow showing the direction
def indent(direction):
    #
    # Full width lines top and bottom, the indented lines in between, and the
    # arrow always on the left where the indent itself happens.
    out = [rect(3, 3.4, 18, 2.2), rect(3, 18.1, 18, 2.2),
           rect(10.4, 8.3, 10.6, 2.2), rect(10.4, 13.2, 10.6, 2.2)]
    if direction == 'right':
        out.append(f"M{n(3)},{n(8.1)}l{n(4.8)},{n(3.9)}l{n(-4.8)},{n(3.9)}z")
    else:
        out.append(f"M{n(7.8)},{n(8.1)}l{n(-4.8)},{n(3.9)}l{n(4.8)},{n(3.9)}z")
    return out
made.append(write('are_ic_indentright', indent('right')))
made.append(write('are_ic_indentleft', indent('left')))

# ---------------------------------------------------------------- blocks
# horizontal rule: a solid rule between two lines of text
made.append(write('are_ic_hr', [
    rect(5.5, 4.6, 13, 1.7),
    rect(2, 10.5, 20, 3),
    rect(5.5, 17.7, 13, 1.7)]))

made.append(write('are_ic_image', [
    rrect(2.5, 3.5, 19, 17, 2.5) + rrect(4.7, 5.7, 14.6, 12.6, 1),
    circle(9, 10, 1.7),
    f"M{n(5.2)},{n(17.6)}l{n(4.2)},{n(-5)}l{n(2.6)},{n(3.1)}l{n(2.6)},{n(-3.1)}"
    f"l{n(4.2)},{n(5)}z"], even_odd=True))

made.append(write('are_ic_video', [
    rrect(2.5, 4.5, 19, 15, 2.5) + rrect(4.7, 6.7, 14.6, 10.6, 1),
    f"M{n(10)},{n(8.6)}l{n(6)},{n(3.4)}l{n(-6)},{n(3.4)}z"], even_odd=True))

made.append(write('are_ic_emoji', [
    circle(12, 12, 9.5) + circle(12, 12, 7.6),
    circle(9, 10, 1.3), circle(15, 10, 1.3),
    f"M{n(7.6)},{n(13.6)}h{n(1.9)}a{n(2.6)},{n(2.6)} 0 0 0 {n(5)},0"
    f"h{n(1.9)}a{n(4.5)},{n(4.5)} 0 0 1 {n(-8.8)},0z"], even_odd=True))

# link / unlink: two chain halves plus the bar between them
LINK_L = (f"M{n(10.2)},{n(7.2)}h{n(-3.4)}"
          f"a{n(4.8)},{n(4.8)} 0 0 0 0,{n(9.6)}h{n(3.4)}v{n(-2.3)}h{n(-3.4)}"
          f"a{n(2.5)},{n(2.5)} 0 0 1 0,{n(-5)}h{n(3.4)}z")
LINK_R = (f"M{n(13.8)},{n(7.2)}h{n(3.4)}"
          f"a{n(4.8)},{n(4.8)} 0 0 1 0,{n(9.6)}h{n(-3.4)}v{n(-2.3)}h{n(3.4)}"
          f"a{n(2.5)},{n(2.5)} 0 0 0 0,{n(-5)}h{n(-3.4)}z")
made.append(write('are_ic_link', [LINK_L, LINK_R, rect(8.4, 10.9, 7.2, 2.2)]))
made.append(write('are_ic_unlink', [LINK_L, LINK_R,
    f"M{n(5.1)},{n(3.4)}l{n(1.7)},{n(-1.7)}l{n(15.8)},{n(15.8)}"
    f"l{n(-1.7)},{n(1.7)}z"]))

# keyboard: the emoji button toggles to this while the panel is open
KEY = 2.4
made.append(write('are_ic_keyboard', [
    rrect(1.5, 4.5, 21, 15, 2.5) + rrect(3.7, 6.7, 16.6, 10.6, 1),
    ] + [rect(x, 8.4, KEY, KEY) for x in (5.2, 9.1, 13, 16.9)]
      + [rect(x, 11.9, KEY, KEY) for x in (5.2, 16.9)]
      + [rect(8.4, 11.9, 7.2, KEY), rect(7.6, 15.1, 8.8, 2.2)],
    even_odd=True))

print('\n'.join(made))
print('total:', len(made))
