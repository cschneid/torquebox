require 'java'

$: << File.dirname(__FILE__) + '/../lib' 

require 'vfs'


def fix_windows_path(path)
  if ( path =~ /^([A-Z])(:.*)/ )
    path = "#{$1.downcase}#{$2}"
  end

  if ( ! ( path =~ %r(^/.*) ) )
    path = "/#{path}"
  end
end

TEST_DATA_BASE = 'target/test-data'
TEST_DATA_DIR = fix_windows_path( File.expand_path( File.join( File.dirname(__FILE__), "/../#{TEST_DATA_BASE}" ) ) )

