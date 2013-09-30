class Screen(object):
    def __init__(self, text=''):
        self._text = text
        self._clip_board = ''

    @property
    def text(self):
        return self._text

    @property
    def clipboard(self):
        return self._clip_board

    def copy(self, start=0, end=0):
        self._clip_board = self._text[start:end]

    def cut(self, start=0, end=0):
        self._clip_board = self._text[start:end]
        self._text = self._text[:start] + self._text[end:]

    def paste(self, offset=0):
        self._text = self._text[:offset] + self._clip_board + self._text[offset:]

    def clear_clipboard(self):
        self._clip_board = ''

    def length(self):
        return len(self._text)

    def __str__(self):
        return self._text

