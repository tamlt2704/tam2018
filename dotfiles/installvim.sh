 git clone https://github.com/vim/vim.git
 cd vim/
 ./configure --enable-pythoninterp --with-features=huge --prefix=$HOME/opt/vim
 make && make install
 mkdir -p $HOME/bin
 cd $HOME/bin
 ls -s $HOME/opt/vim/bin/vim vim
vim --version | grep python
