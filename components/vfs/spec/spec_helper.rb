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

  path
end

TEST_DATA_BASE = 'target/test-data'
test_data_dir = File.expand_path( File.join( File.dirname(__FILE__), "/../#{TEST_DATA_BASE}" ))
puts "test_data_dir.1=#{test_data_dir}"
TEST_DATA_DIR = fix_windows_path( test_data_dir )
puts "TEST_DATA_DIR.1=#{TEST_DATA_DIR}"

TESTING_ON_WINDOWS = ( java.lang::System.getProperty( "os.name" ) =~ /windows/i )
