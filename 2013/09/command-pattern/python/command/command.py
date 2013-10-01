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


class Command(object):
    _previous_status = ''

    def execute(self):
        pass

    def undo(self):
        pass


class CopyCommand(Command):
    def __init__(self, screen, start=0, end=0):
        self._screen = screen
        self._start = start
        self._end = end
        self._previous_status = screen.text

    def execute(self):
        self._screen.copy(start=self._start, end=self._end)

    def undo(self):
        self._screen.clear_clipboard()

class CutCommand(Command):
    def __init__(self, screen, start=0, end=0):
        self._screen = screen
        self._start = start
        self._end = end
        self._previous_status = screen.text

    def execute(self):
        self._screen.cut(start=self._start, end=self._end)

    def undo(self):
        self._screen.clear_clipboard()
        self._screen._text = self._previous_status

class PasteCommand(Command):
    def __init__(self, screen, offset=0):
        self._screen = screen
        self._offset = offset
        self._previous_status = screen.text

    def execute(self):
        self._screen.paste(offset=self._offset)

    def undo(self):
        self._screen.clear_clipboard()
        self._screen._text = self._previous_status
