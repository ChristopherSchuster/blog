import unittest

from command import Screen

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

def main():
    suite1 = unittest.TestLoader().loadTestsFromTestCase(CommandTestCase)
    alltests = unittest.TestSuite([suite1])
    unittest.TextTestRunner(verbosity=2).run(alltests)


if __name__ == '__main__':
    main()
