
require File.dirname(__FILE__) +  '/spec_helper.rb'

describe "VFS::Dir" do

  before(:each) do
    @executor = java.util.concurrent::Executors.newScheduledThreadPool( 1 )
    @temp_file_provider = org.jboss.vfs::TempFileProvider.create( "vfs-test", @executor )
    @archive1_path = fix_windows_path( File.expand_path( "#{TEST_DATA_DIR}/home/larry/archive1.jar" ) )
    @archive1_file = org.jboss.vfs::VFS.child( @archive1_path )
    @archive1_mount_point = org.jboss.vfs::VFS.child( @archive1_path )
    @archive1_handle = org.jboss.vfs::VFS.mountZip( @archive1_file, @archive1_mount_point, @temp_file_provider )

    #@archive1_path = fix_windows_path( @archive1_path )
  end

  after(:each) do
    @archive1_handle.close
  end

  describe "entries" do
    it "should find vfs entries outside of archives" do
      path = "#{@archive1_path}/.."
      real_dir = ::Dir.new( path )

      real_dir.entries.size.should eql( 5 )
      real_dir.entries.should include('.')
      real_dir.entries.should include('..')
      real_dir.entries.should include('archive1.jar')
      real_dir.entries.should include('file1.txt')
      real_dir.entries.should include('file2.txt')

      vfs_dir = VFS::Dir.new( "vfs:#{fix_windows_path(path)}" )

      vfs_dir.entries.size.should eql( 5 )
      vfs_dir.entries.should include('.')
      vfs_dir.entries.should include('..')
      vfs_dir.entries.should include('archive1.jar')
      vfs_dir.entries.should include('file1.txt')
      vfs_dir.entries.should include('file2.txt')

    end

    it "should find vfs entries inside of archives" do
      path = "vfs:#{fix_windows_path(@archive1_path)}/other_lib/subdir"
      puts "doing path #{path}"
      entries = VFS::Dir.new( path ).entries
      entries.size.should == 3
      entries.should include( "." )
      entries.should include( ".." )
      entries.should include( "archive6.jar" )
    end
  end
end
