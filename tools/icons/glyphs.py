"""Extract glyph outlines as VectorDrawable pathData."""
from fontTools.ttLib import TTFont, TTCollection
from fontTools.pens.svgPathPen import SVGPathPen
from fontTools.pens.boundsPen import BoundsPen
from fontTools.pens.transformPen import TransformPen
from fontTools.pens.recordingPen import RecordingPen
from fontTools.misc.transform import Transform

FONTS = {
    'bold': '/System/Library/Fonts/Supplemental/Arial Bold.ttf',
    'regular': '/System/Library/Fonts/Supplemental/Arial.ttf',
    'italic': '/System/Library/Fonts/Supplemental/Arial Bold Italic.ttf',
    'black': '/System/Library/Fonts/Supplemental/Arial Black.ttf',
}
_cache = {}

def font(style):
    if style not in _cache:
        _cache[style] = TTFont(FONTS[style])
    return _cache[style]

def glyph_path(char, style='bold'):
    f = font(style)
    cmap = f.getBestCmap()
    name = cmap[ord(char)]
    gs = f.getGlyphSet()
    rec = RecordingPen()
    gs[name].draw(rec)
    bp = BoundsPen(gs)
    rec.replay(bp)
    return rec, bp.bounds, gs

def fit(char, style, box, align='center', keep_aspect=True, round_to=2):
    """Return pathData for `char` scaled into box=(x, y, w, h) in a 24x24 grid."""
    rec, bounds, gs = glyph_path(char, style)
    x0, y0, x1, y1 = bounds
    gw, gh = x1 - x0, y1 - y0
    bx, by, bw, bh = box
    if keep_aspect:
        s = min(bw / gw, bh / gh)
        sx = sy = s
    else:
        sx, sy = bw / gw, bh / gh
    w, h = gw * sx, gh * sy
    if align == 'center':
        ox = bx + (bw - w) / 2
    elif align == 'left':
        ox = bx
    else:
        ox = bx + (bw - w)
    oy = by + (bh - h) / 2
    # font space is Y-up, vector drawable is Y-down
    t = Transform(sx, 0, 0, -sy, ox - x0 * sx, oy + h + y0 * sy)
    out = SVGPathPen(gs, ntos=lambda v: str(round(v, round_to)))
    rec.replay(TransformPen(out, t))
    return out.getCommands()

if __name__ == '__main__':
    import sys
    print(fit(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else 'bold',
              (2, 2, 20, 20)))
