import unittest

from command import Screen, CopyCommand, CutCommand, PasteCommand

class CommandTestCase(unittest.TestCase):
    def test_copy(self):
        screen = Screen('hello world!!')
        screen.copy(start=4, end=8)
        self.assertEquals('o wo', screen.clipboard)
        self.assertEquals('hello world!!', screen.text)

    def test_paste(self):
        screen = Screen('hello world!!')
        screen.copy(start=0, end=5)
        screen.paste(screen.length())
        self.assertEquals('hello world!!hello', screen.text)

    def test_cut(self):
        screen = Screen('hello world!!')
        screen.cut(start=5, end=11)
        self.assertEquals('hello!!', screen.text)
        self.assertEquals(' world', screen.clipboard)

    def test_copy_command(self):
        screen = Screen('hello world!!')
        command = CopyCommand(screen, start=4, end=8)
        command.execute()
        self.assertEquals('o wo', screen.clipboard)
        self.assertEquals('hello world!!', screen.text)
        command.undo()
        self.assertEquals('', screen.clipboard)

    def test_cut_command(self):
        screen = Screen('hello world!!')
        command = CutCommand(screen, start=5, end=11)
        command.execute()
        self.assertEquals('hello!!', screen.text)
        self.assertEquals(' world', screen.clipboard)
        command.undo()
        self.assertEquals('hello world!!', screen.text)

    def test_paste_command(self):
        screen = Screen('hello world!!')
        cut_command = CutCommand(screen, start=5, end=11)
        cut_command.execute()
        self.assertEquals(' world', screen.clipboard)
        paste_command = PasteCommand(screen, offset=0)
        paste_command.execute()
        self.assertEquals(' worldhello!!', screen.text)
        paste_command.undo()
        self.assertEquals('hello!!', screen.text)

def main():
    suite1 = unittest.TestLoader().loadTestsFromTestCase(CommandTestCase)
    alltests = unittest.TestSuite([suite1])
    unittest.TextTestRunner(verbosity=2).run(alltests)


if __name__ == '__main__':
    main()
